
import React, { useEffect, useRef } from 'react';
import * as THREE from 'three';
import { Emotion, EmotionColors, EstadoConsciencia } from '../types';

interface Sylph3DProps {
  emotion: Emotion;
  estado: EstadoConsciencia;
  isProcessing: boolean;
}

const Sylph3D: React.FC<Sylph3DProps> = ({ emotion, estado, isProcessing }) => {
  const mountRef = useRef<HTMLDivElement>(null);
  const rendererRef = useRef<THREE.WebGLRenderer | null>(null);
  const sceneRef = useRef<THREE.Scene | null>(null);
  const modelRef = useRef<{
    torso: THREE.Mesh;
    head: THREE.Mesh;
    neck: THREE.Mesh;
  } | null>(null);

  useEffect(() => {
    if (!mountRef.current) return;

    const width = 240;
    const height = 320;

    const scene = new THREE.Scene();
    sceneRef.current = scene;

    const camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000);
    camera.position.z = 40;

    const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setSize(width, height);
    rendererRef.current = renderer;
    mountRef.current.appendChild(renderer.domElement);

    const ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
    scene.add(ambientLight);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1.2);
    directionalLight.position.set(5, 5, 5);
    scene.add(directionalLight);

    const skinMaterial = new THREE.MeshStandardMaterial({ color: 0xe0e0e0, roughness: 0.3, metalness: 0.4 });
    const clothingMaterial = new THREE.MeshStandardMaterial({ color: EmotionColors[emotion], roughness: 0.7, metalness: 0.1 });

    const torsoGeometry = new THREE.CylinderGeometry(5, 4, 18, 32);
    const torso = new THREE.Mesh(torsoGeometry, clothingMaterial);
    scene.add(torso);

    const headGeometry = new THREE.SphereGeometry(5.5, 32, 32);
    const head = new THREE.Mesh(headGeometry, skinMaterial);
    head.position.y = 15;
    scene.add(head);

    const neckGeometry = new THREE.CylinderGeometry(1.5, 2, 4, 32);
    const neck = new THREE.Mesh(neckGeometry, skinMaterial);
    neck.position.y = 11;
    scene.add(neck);

    modelRef.current = { torso, head, neck };

    let animationFrameId: number;
    let time = 0;

    const animate = () => {
      animationFrameId = requestAnimationFrame(animate);
      time += 0.02;

      if (modelRef.current) {
        const { torso, head } = modelRef.current;
        
        let breathSpeed = isProcessing ? 8 : 2;
        torso.scale.y = 1 + Math.sin(time * breathSpeed) * 0.03;
        
        head.rotation.y = Math.sin(time * 0.5) * 0.15;
        head.rotation.x = Math.cos(time * 0.3) * 0.08;

        if (estado === EstadoConsciencia.DORMINDO) {
          head.rotation.x = 0.4;
          torso.scale.y = 1 + Math.sin(time * 0.5) * 0.01;
        }

        clothingMaterial.color.set(EmotionColors[emotion]);
      }

      renderer.render(scene, camera);
    };

    animate();

    return () => {
      cancelAnimationFrame(animationFrameId);
      if (mountRef.current && rendererRef.current) {
        mountRef.current.removeChild(rendererRef.current.domElement);
      }
      renderer.dispose();
    };
  }, [emotion, estado, isProcessing]);

  return (
    <div className="relative group">
      <div className="avatar-aura" style={{ borderColor: EmotionColors[emotion] }} />
      <div ref={mountRef} className="z-10" />
      <div className="absolute -bottom-4 left-1/2 -translate-x-1/2 flex flex-col items-center">
         <div className="px-3 py-1 bg-black/80 backdrop-blur-md border border-white/10 rounded-full">
            <span className="text-[10px] font-bold tracking-[0.2em] text-white uppercase" style={{ color: EmotionColors[emotion] }}>
              {estado}
            </span>
         </div>
      </div>
    </div>
  );
};

export default Sylph3D;
